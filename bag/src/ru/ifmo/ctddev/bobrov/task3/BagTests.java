package ru.ifmo.ctddev.bobrov.task3;

import junit.framework.Assert;
import org.junit.Test;
import ru.ifmo.ctddev.bobrov.task3.bag.Bag;

import java.util.*;

public class BagTests {
    @Test
    public void testConstruction() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        Bag<String> bag = new Bag<>(list);
        Assert.assertEquals(bag.size(), list.size());
        Assert.assertTrue(bag.containsAll(list));
    }

    @Test
    public void testIteration() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb");
        Bag<String> bag = new Bag<>(list);
        HashMap<String, Integer> prev = new HashMap<>();
        String lastGroup = null;
        int counter = 0;
        for (String last : bag) {
            counter++;
            if (!last.equals(lastGroup)) {
                if (prev.containsKey(last)) {
                    Assert.assertTrue("Iteration order corrupted", false);
                }
                counter = 1;
                lastGroup = last;
            }
            prev.put(lastGroup, counter);
        }
        HashMap<String, Integer> expected = new HashMap<>();
        for (String o : list) {
            if (!expected.containsKey(o)) {
                expected.put(o, 1);
            } else {
                expected.put(o, expected.get(o) + 1);
            }
        }
        Assert.assertEquals(expected, prev);
    }

    @Test
    public void testNullElements() {
        new Bag<>(Arrays.asList((Object) null));
    }

    @Test
    public void testRemove() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue(bag.contains("ddd"));
        Assert.assertTrue("remove returned false when collection changed", bag.remove("ddd"));
        Assert.assertFalse(bag.contains("ddd"));
        Assert.assertFalse("remove returned true when collection not changed", bag.remove("fff"));
    }

    @Test
    public void testRemoveAll() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("removeAll returned false when collection changed", bag.removeAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertFalse(bag.contains("aaa"));
        Assert.assertFalse(bag.contains("bbb"));
        Assert.assertTrue(bag.contains("ccc"));
        Assert.assertTrue(bag.contains("ddd"));
    }

    @Test
    public void testRetainAll() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertTrue("retainAll returned false when collection changed", bag.retainAll(Arrays.asList("aaa", "bbb", "fff")));
        Assert.assertTrue(bag.contains("aaa"));
        Assert.assertTrue(bag.contains("bbb"));
        Assert.assertFalse(bag.contains("ccc"));
        Assert.assertFalse(bag.contains("ddd"));
    }

    @Test
    public void testClear() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb", "ccc", "bbb", "ccc", "ddd", "aaa", "bbb"));
        Assert.assertFalse(bag.isEmpty());
        bag.clear();
        Assert.assertTrue(bag.isEmpty());
    }

    @Test
    public void testIteratorRemove() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        while (!"ddd".equals(it.next())) {
        }
        it.remove();
        Assert.assertFalse(bag.contains("ddd"));
    }

    @Test
    public void testIteratorRemoveMultiple() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "ddd", "eee", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
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
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        it.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorDoubleRemove() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        it.next();
        it.next();
        it.remove();
        it.remove();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorConcurrentModification() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "ddd", "bbb", "ccc", "bbb", "ccc", "aaa", "bbb"));
        Iterator it = bag.iterator();
        bag.add("fff");
        it.next();
    }

//    @Test
//    public void testAddSelf() {
//        Bag bag = new Bag(Arrays.asList("aaa", "bbb"));
//        Assert.assertTrue(bag.addAll(bag));
//        Assert.assertEquals(4, bag.size());
//    }

    @Test
    public void testRemoveAllSelf() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb"));
        Assert.assertTrue(bag.removeAll(bag));
        Assert.assertTrue(bag.isEmpty());
        Assert.assertFalse(bag.removeAll(bag));
    }

    @Test
    public void testRetainAllSelf() {
        Bag<String> bag = new Bag<>(Arrays.asList("aaa", "bbb"));
        Assert.assertFalse(bag.retainAll(bag));
    }
}
