package cn.com.swain.baselib.Queue;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class SyncLimitQueue<E> implements Queue<E> {

    private LimitQueue<E> queue;

    private final Object mutex;

    public SyncLimitQueue(int limit) {
        queue = new LimitQueue<>(limit);
        mutex = this;
    }

    @Override
    public int size() {
        synchronized (mutex) {
            return queue.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (mutex) {
            return queue.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {
        synchronized (mutex) {
            return queue.contains(o);
        }
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        synchronized (mutex) {
            return queue.iterator();
        }
    }

    @NonNull
    @Override
    public Object[] toArray() {
        synchronized (mutex) {
            return queue.toArray();
        }
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        synchronized (mutex) {
            return queue.toArray(a);
        }
    }

    @Override
    public boolean add(E e) {
        synchronized (mutex) {
            return queue.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (mutex) {
            return queue.remove(o);
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        synchronized (mutex) {
            return queue.containsAll(c);
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        synchronized (mutex) {
            return queue.addAll(c);
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        synchronized (mutex) {
            return queue.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        synchronized (mutex) {
            return queue.retainAll(c);
        }
    }

    @Override
    public void clear() {
        synchronized (mutex) {
            queue.clear();
        }
    }

    @Override
    public boolean offer(E e) {
        synchronized (mutex) {
            return queue.offer(e);
        }
    }

    @Override
    public E remove() {
        synchronized (mutex) {
            return queue.remove();
        }
    }

    @Override
    public E poll() {
        synchronized (mutex) {
            return queue.poll();
        }
    }

    @Override
    public E element() {
        synchronized (mutex) {
            return queue.element();
        }
    }

    @Override
    public E peek() {
        synchronized (mutex) {
            return queue.peek();
        }
    }
}
