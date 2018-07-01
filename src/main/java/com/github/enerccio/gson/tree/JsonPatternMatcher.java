package com.github.enerccio.gson.tree;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.enerccio.gson.tree.TreeInfo.TreeInfoType;

/**
 * Constructs matcher for tree pattern. This class is thread safe. </br>
 * </br>
 * Pattern is parsed with this grammar: </br>
 * </br>
 * path = path_content (path_separator path_element+)? </br>
 * path_separator = "." </br>
 * path_element = path_separator path_content </br>
 * path_content = property | index | treeany | any </br>
 * treeany = "**" </br>
 * any = "*" </br>
 * index = "@" number </br>
 * property = string </br>
 * </br>
 * Example pattern: "prop1.@3.*.qux.**.foo" will match this path:
 * prop1.@3.qux.a.@2.foo </br>
 * Both treeany and any will match empty trees. \ to escape syntax elements
 * 
 * @author pvan
 * @since 1.1.0
 */
public class JsonPatternMatcher {

	private enum MatchResult {
		NOT_MATCHED, MATCHED, MATCHED_STAY, MATCHED_MOVE,
	}

	private static abstract class MatchTreeElement {

		public abstract MatchResult match(TreeInfo info);

		@Override
		public String toString() {
			return this.getClass().getSimpleName();
		}

	}

	private static class MatchIndex extends MatchTreeElement {

		private final int index;

		public MatchIndex(int index) {
			this.index = index;
		}

		@Override
		public MatchResult match(TreeInfo info) {
			if (info.getType() == TreeInfoType.INDEX && info.getIndex() == index)
				return MatchResult.MATCHED;
			return MatchResult.NOT_MATCHED;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "=\"" + index + "\"";
		}
	}

	private static class MatchProperty extends MatchTreeElement {

		private final String property;

		public MatchProperty(String property) {
			this.property = property;
		}

		@Override
		public MatchResult match(TreeInfo info) {
			if (info.getType() == TreeInfoType.PROPERTY && property.equals(info.getProperty()))
				return MatchResult.MATCHED;
			return MatchResult.NOT_MATCHED;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "=\"" + property + "\"";
		}

	}

	private static abstract class MatchMultiMatch extends MatchTreeElement {

		protected MatchTreeElement terminalMatch;

		public void setTerminal(MatchTreeElement terminalMatch) {
			this.terminalMatch = terminalMatch;
		}

	}

	private static class MatchTreeAny extends MatchMultiMatch {

		@Override
		public MatchResult match(TreeInfo info) {
			if (terminalMatch != null) {
				if (terminalMatch.match(info) == MatchResult.MATCHED)
					return MatchResult.MATCHED_MOVE;
			}
			return MatchResult.MATCHED_STAY;
		}

	}

	private static class MatchAny extends MatchMultiMatch {

		@Override
		public MatchResult match(TreeInfo info) {
			if (terminalMatch != null) {
				if (terminalMatch.match(info) == MatchResult.MATCHED)
					return MatchResult.MATCHED_MOVE;
			}
			return MatchResult.MATCHED;
		}

	}

	/**
	 * Compiles matcher from pattern
	 * <p>
	 * Alternative to constructor call that handles pattern parse exception.
	 * 
	 * @param pattern
	 *            patter to compile
	 * @return compiled matcher or null on failure
	 * @since 1.1.0
	 */
	public static JsonPatternMatcher compile(String pattern) {
		try {
			return new JsonPatternMatcher(pattern);
		} catch (ParseException e) {
			return null;
		}
	}

	private final List<MatchTreeElement> compiledPattern;

	/**
	 * Compiles this pattern to a matcher
	 * 
	 * @param pattern
	 * @throws ParseException
	 */
	public JsonPatternMatcher(String pattern) throws ParseException {
		compiledPattern = compilePattern(pattern);
	}

	private enum TokenType {
		DOT, WILDCARD, AT, VALUE
	}

	private static class Token {

		private final TokenType type;
		private final String value;
		private final int offset;

		public Token(TokenType type, int offset) {
			this(type, null, offset);
		}

		public Token(TokenType type, String value, int offset) {
			this.type = type;
			this.value = value;
			this.offset = offset;
		}

		@Override
		public String toString() {
			return "Token [type=" + type + ", value=" + value + "]";
		}

	}

	/**
	 * Compiles pattern to a compiled pattern
	 * 
	 * @param pattern
	 *            to compile
	 * @return compiled pattern
	 * @throws ParseException
	 *             when pattern is invalid
	 * @since 1.1.0
	 */
	private List<MatchTreeElement> compilePattern(String pattern) throws ParseException {
		List<MatchTreeElement> cp = new ArrayList<MatchTreeElement>();

		List<Token> tokens = tokenize(pattern);
		Set<TokenType> expectedToken = typeSet(TokenType.WILDCARD, TokenType.VALUE, TokenType.AT);
		List<MatchMultiMatch> awaitingTerminal = new ArrayList<MatchMultiMatch>();

		boolean buildingNumber = false;
		boolean buildingWildcard = false;

		for (Token t : tokens) {
			if (!expectedToken.contains(t.type)) {
				throw new ParseException("Unparseable pattern. Got " + t.type + ", expected " + expectedToken,
						t.offset);
			}

			switch (t.type) {
			case AT:
				buildingNumber = true;
				expectedToken = typeSet(TokenType.VALUE);
				break;
			case DOT:
				if (buildingWildcard) {
					buildingWildcard = false;
					MatchAny any = new MatchAny();
					awaitingTerminal.add(any);
					cp.add(any);
				}
				expectedToken = typeSet(TokenType.WILDCARD, TokenType.VALUE, TokenType.AT);
				break;
			case VALUE:
				MatchTreeElement terminal;
				if (buildingNumber) {
					try {
						terminal = new MatchIndex(Integer.parseInt(t.value));
					} catch (NumberFormatException e) {
						throw new ParseException("Expected number, got " + t.value, t.offset);
					}
				} else {
					terminal = new MatchProperty(t.value);
				}
				buildingNumber = false;
				expectedToken = typeSet(TokenType.DOT);
				for (MatchMultiMatch matchAny : awaitingTerminal) {
					matchAny.setTerminal(terminal);
				}
				awaitingTerminal.clear();
				cp.add(terminal);
				break;
			case WILDCARD:
				if (buildingWildcard) {
					buildingWildcard = false;
					MatchTreeAny treeAny = new MatchTreeAny();
					cp.add(treeAny);
					awaitingTerminal.add(treeAny);
					expectedToken = typeSet(TokenType.DOT);
				} else {
					buildingWildcard = true;
					expectedToken = typeSet(TokenType.WILDCARD, TokenType.DOT);
				}
				break;
			}
		}

		// filter all tail wildcards since they are useless
		List<MatchTreeElement> filtered = new ArrayList<MatchTreeElement>();
		List<MatchTreeElement> wildcards = new ArrayList<MatchTreeElement>();

		for (MatchTreeElement e : cp) {
			if ((e instanceof MatchTreeAny) || (e instanceof MatchAny)) {
				wildcards.add(e);
			} else {
				filtered.addAll(wildcards);
				filtered.add(e);
				wildcards.clear();
			}
		}

		return filtered;
	}

	private static Set<TokenType> typeSet(TokenType... tokenTypes) {
		return new HashSet<TokenType>(Arrays.asList(tokenTypes));
	}

	private List<Token> tokenize(String pattern) throws ParseException {
		List<Token> tokenList = new ArrayList<Token>();

		boolean escape = false;
		StringBuffer cStringBuffer = new StringBuffer();
		int valueTokenStartPos = 0;

		for (int i = 0; i < pattern.length(); i++) {
			char c = pattern.charAt(i);

			Token finalizedToken = null;

			if (!escape && c == '.') {
				finalizedToken = new Token(TokenType.DOT, i);
			} else if (!escape && c == '\\') {
				escape = true;
			} else if (!escape && c == '@') {
				finalizedToken = new Token(TokenType.AT, i);
			} else if (!escape && c == '*') {
				finalizedToken = new Token(TokenType.WILDCARD, i);
			} else {
				cStringBuffer.append(c);
			}

			if (finalizedToken != null) {
				if (cStringBuffer.length() != 0) {
					String tokenValue = cStringBuffer.toString();
					tokenList.add(new Token(TokenType.VALUE, tokenValue, valueTokenStartPos));
					cStringBuffer = new StringBuffer();
					valueTokenStartPos = i + 1;
				}
				tokenList.add(finalizedToken);
			}
		}

		if (cStringBuffer.length() != 0) {
			String tokenValue = cStringBuffer.toString();
			tokenList.add(new Token(TokenType.VALUE, tokenValue, valueTokenStartPos));
		}

		return tokenList;
	}

	/**
	 * Matches list of tree infos to this macher
	 * <p>
	 * This method is thread safe.
	 * 
	 * @param path
	 *            list of {@link TreeInfo} path elements
	 * @return whether this matcher matches or not
	 * @since 1.1.0
	 */
	public boolean matches(Collection<TreeInfo> path) {
		int matchPos = 0;
		for (TreeInfo nfo : path) {
			if (matchPos >= compiledPattern.size()) {
				return true; // no more tests == matches
			}
			MatchTreeElement e = compiledPattern.get(matchPos);

			switch (e.match(nfo)) {
			case MATCHED_MOVE:
				++matchPos;
			case MATCHED:
				++matchPos;
				break;
			case MATCHED_STAY:
				break;
			case NOT_MATCHED:
				return false;
			default:
				break;
			}
		}
		return matchPos >= compiledPattern.size();
	}
}
