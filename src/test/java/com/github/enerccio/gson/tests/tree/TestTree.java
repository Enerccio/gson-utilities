package com.github.enerccio.gson.tests.tree;

import java.text.ParseException;
import java.util.Arrays;

import com.github.enerccio.gson.tree.JsonPatternMatcher;
import com.github.enerccio.gson.tree.TreeInfo;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestTree extends TestCase {
	
	public TestTree(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TestTree.class);
	}

	public void testMatcherCompilation() throws ParseException {
		
		@SuppressWarnings("unused")
		JsonPatternMatcher matcher;
		
		matcher = new JsonPatternMatcher("foo");
		matcher = new JsonPatternMatcher("foo.bar");
		matcher = new JsonPatternMatcher("@2");
		matcher = new JsonPatternMatcher("**.x");
		matcher = new JsonPatternMatcher("foo.bar.baz.@1.*.x.**.z");
		
		try {
			matcher = new JsonPatternMatcher(".x");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}
		try {
			matcher = new JsonPatternMatcher("@a");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}
		try {
			matcher = new JsonPatternMatcher("a.***");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}
		try {
			matcher = new JsonPatternMatcher("@@");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}
		try {
			matcher = new JsonPatternMatcher("..");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}
		try {
			matcher = new JsonPatternMatcher("@4@2");
			throw new AssertionError();
		} catch (ParseException e) {
			e.getMessage();
		}		
	}
	
	public void testMatchers() throws ParseException {
		
		TreeInfo[] nothing = new TreeInfo[0];
		TreeInfo[] i0 = new TreeInfo[] { new TreeInfo(0) };
		TreeInfo[] pfoo = new TreeInfo[] { new TreeInfo("foo") };
		TreeInfo[] pfooi0 = new TreeInfo[] { new TreeInfo("foo"), new TreeInfo(0) };
		TreeInfo[] pfooi1 = new TreeInfo[] { new TreeInfo("foo"), new TreeInfo(1) };
		TreeInfo[] pfoopbar = new TreeInfo[] { new TreeInfo("foo"), new TreeInfo("bar") };
		TreeInfo[] pfoopbarpqux = new TreeInfo[] { new TreeInfo("foo"), new TreeInfo("bar"), new TreeInfo("qux") };
		TreeInfo[] pfoopqux = new TreeInfo[] { new TreeInfo("foo"), new TreeInfo("qux") };
		
		JsonPatternMatcher matcher;
		
		// test identity macher
		matcher = new JsonPatternMatcher("");
		testMatcher(matcher, true, nothing);
		testMatcher(matcher, true, i0);
		testMatcher(matcher, true, pfoo);
		testMatcher(matcher, true, pfooi0);
		testMatcher(matcher, true, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, true, pfoopqux);
		
		// simple property match		
		matcher = new JsonPatternMatcher("foo");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, true, pfoo);
		testMatcher(matcher, true, pfooi0);
		testMatcher(matcher, true, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, true, pfoopqux);
		
		// simple index match
		matcher = new JsonPatternMatcher("@0");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, true, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, false, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		
		// multi values
		matcher = new JsonPatternMatcher("i0.abc");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, false, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.@1");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, true, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, false, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.@1.qzux");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, false, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.bar");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.bar.qux");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		
		// test wildcards
		matcher = new JsonPatternMatcher("foo.*");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, true, pfoo);
		testMatcher(matcher, true, pfooi0);
		testMatcher(matcher, true, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, true, pfoopqux);
		matcher = new JsonPatternMatcher("foo.*.bar");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.bar.*");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, true, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		matcher = new JsonPatternMatcher("foo.bar.*.qux");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, false, pfoopqux);
		
		// multi wildcard
		matcher = new JsonPatternMatcher("foo.**.qux");
		testMatcher(matcher, false, nothing);
		testMatcher(matcher, false, i0);
		testMatcher(matcher, false, pfoo);
		testMatcher(matcher, false, pfooi0);
		testMatcher(matcher, false, pfooi1);
		testMatcher(matcher, false, pfoopbar);
		testMatcher(matcher, true, pfoopbarpqux);
		testMatcher(matcher, true, pfoopqux);
	}

	private void testMatcher(JsonPatternMatcher matcher, boolean b, TreeInfo... treeInfos) {
		if (b) {
			Assert.assertTrue(matcher.matches(Arrays.asList(treeInfos)));
		} else {
			Assert.assertFalse(matcher.matches(Arrays.asList(treeInfos)));
		}
	}
}
