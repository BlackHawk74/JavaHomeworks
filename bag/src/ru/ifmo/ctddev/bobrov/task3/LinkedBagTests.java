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
        List list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        LinkedBag bag = new LinkedBag(list);
        Assert.assertEquals(bag.size(), list.size());
        Assert.assertTrue(bag.containsAll(list));
    }

    @Test
    public void testIteration() {
        List list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        LinkedBag bag = new LinkedBag(list);
        Iterator it1 = list.iterator(), it2 = bag.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Assert.assertEquals(it1.next(), it2.next());
        }
        Assert.assertTrue(!it1.hasNext() && !it2.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testNullElements() {
        new LinkedBag(Arrays.asList(null));
    }

    @Test
    public void testRemove() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue(bag.contains("ddd"));
        Assert.assertTrue("remove returned false when collection changed", bag.remove("ddd"));
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertFalse("remove returned true when collection not changed", bag.remove("fff"));
    }

    @Test
    public void testRemoveAll() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("removeAll returned false when collection changed", bag.removeAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertFalse(bag.contains("aaa"));
        Assert.assertFalse(bag.contains("bbb"));
        Assert.assertTrue(bag.contains("ccc"));
        Assert.assertTrue(bag.contains("ddd"));
        Assert.assertEquals(3, bag.size());
    }

    @Test
    public void testRetainAll() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("retainAll returned false when collection changed", bag.retainAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertTrue(bag.contains("aaa"));
        Assert.assertTrue(bag.contains("bbb"));
        Assert.assertFalse(bag.contains("ccc"));
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertEquals(5, bag.size());
    }

    @Test
    public void testClear() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertFalse(bag.isEmpty());
        bag.clear();
        Assert.assertTrue(bag.isEmpty());
    }

    @Test
    public void testIteratorRemove() {
        List list = Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb");
        LinkedBag bag = new LinkedBag(list);
        Iterator it = bag.iterator();
        it.next();
        it.next();
        it.remove();
        Assert.assertFalse(bag.contains("ddd"));
        it = bag.iterator();
        Iterator it2 = list.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Object next = it2.next();
            if ("ddd".equals(next)) {
                next = it2.next();
            }

            Assert.assertEquals(next, it.next());
        }
        Assert.assertTrue(!it.hasNext() && !it2.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorInvalidRemove() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        it.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorDoubleRemove() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        it.next();
        it.next();
        it.remove();
        it.remove();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorConcurrentModification() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        bag.add("fff");
        it.next();
    }


//    @Test
//    public void testAddSelf() {
//        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb"));
//        Assert.assertTrue(bag.addAll(bag));
//        Assert.assertEquals(4, bag.size());
//    }

    @Test
    public void testRemoveAllSelf() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb"));
        Assert.assertTrue(bag.removeAll(bag));
        Assert.assertTrue(bag.isEmpty());
        Assert.assertFalse(bag.removeAll(bag));
    }

    @Test
    public void testRetainAllSelf() {
        LinkedBag bag = new LinkedBag(Arrays.asList("aaa", "bbb"));
        Assert.assertFalse(bag.retainAll(bag));
    }
}
