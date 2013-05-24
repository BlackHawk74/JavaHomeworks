package ru.ifmo.ctddev.bobrov.task3;


import junit.framework.Assert;
import org.junit.Test;
import ru.ifmo.ctddev.bobrov.task3.bag.LinkedBag;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class LinkedBagTests {
    @Test
    public void testConstruction() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        LinkedBag<String> bag = new LinkedBag<>(list);
        Assert.assertEquals(bag.size(), list.size());
        Assert.assertTrue(bag.containsAll(list));
    }

    @Test
    public void testIteration() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        LinkedBag<String> bag = new LinkedBag<>(list);
        Iterator<String> it1 = list.iterator();
        Iterator<String> it2 = bag.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Assert.assertEquals(it1.next(), it2.next());
        }
        Assert.assertTrue(!it1.hasNext() && !it2.hasNext());
    }

    @Test
    public void testNullElements() {
        new LinkedBag<>(Arrays.asList((String) null));
    }

    @Test
    public void testRemove() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue(bag.contains("ddd"));
        Assert.assertTrue("remove returned false when collection changed", bag.remove("ddd"));
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertFalse("remove returned true when collection not changed", bag.remove("fff"));
    }

    @Test
    public void testRemoveAll() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("removeAll returned false when collection changed", bag.removeAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertFalse(bag.contains("aaa"));
        Assert.assertFalse(bag.contains("bbb"));
        Assert.assertTrue(bag.contains("ccc"));
        Assert.assertTrue(bag.contains("ddd"));
        Assert.assertEquals(3, bag.size());
    }

    @Test
    public void testRetainAll() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("retainAll returned false when collection changed", bag.retainAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertTrue(bag.contains("aaa"));
        Assert.assertTrue(bag.contains("bbb"));
        Assert.assertFalse(bag.contains("ccc"));
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertEquals(5, bag.size());
    }

    @Test
    public void testClear() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertFalse(bag.isEmpty());
        bag.clear();
        Assert.assertTrue(bag.isEmpty());
    }

    @Test
    public void testIteratorRemove() {
        List<String> list = Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb");
        LinkedBag<String> bag = new LinkedBag<>(list);
        Iterator<String> it = bag.iterator();
        it.next();
        it.next();
        it.remove();
        Assert.assertFalse(bag.contains("ddd"));
        it = bag.iterator();
        Iterator<String> it2 = list.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Object next = it2.next();
            if ("ddd".equals(next)) {
                next = it2.next();
            }

            Assert.assertEquals(next, it.next());
        }
        Assert.assertTrue(!it.hasNext() && !it2.hasNext());
    }

    @Test
    public void testIteratorRemoveMultiple() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "ddd", "eee", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator<String> it = bag.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if ("ddd".equals(s) || "eee".equals(s)) {
                it.remove();
            }
        }
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertFalse(bag.contains("eee"));
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorInvalidRemove() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator<String> it = bag.iterator();
        it.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorDoubleRemove() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator<String> it = bag.iterator();
        it.next();
        it.next();
        it.remove();
        it.remove();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorConcurrentModification() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator<String> it = bag.iterator();
        bag.add("fff");
        it.next();
    }


//    @Test
//    public void testAddSelf() {
//        LinkedBag<String> bag = new LinkedBag<String>(Arrays.asList("aaa", "bbb"));
//        Assert.assertTrue(bag.addAll(bag));
//        Assert.assertEquals(4, bag.size());
//    }

    @Test
    public void testRemoveAllSelf() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb"));
        Assert.assertTrue(bag.removeAll(bag));
        Assert.assertTrue(bag.isEmpty());
        Assert.assertFalse(bag.removeAll(bag));
    }

    @Test
    public void testRetainAllSelf() {
        LinkedBag<String> bag = new LinkedBag<>(Arrays.asList("aaa", "bbb"));
        Assert.assertFalse(bag.retainAll(bag));
    }
}
