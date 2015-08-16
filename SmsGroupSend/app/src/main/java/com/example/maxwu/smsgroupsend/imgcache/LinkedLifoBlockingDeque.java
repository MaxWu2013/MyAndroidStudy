package com.example.maxwu.smsgroupsend.imgcache;

import java.util.concurrent.TimeUnit;

public class LinkedLifoBlockingDeque<E> extends LinkedBlockingDeque<E> {

	private static final long serialVersionUID = -4854985351588039351L;

	public LinkedLifoBlockingDeque(){
		super();
	}
	
	public LinkedLifoBlockingDeque(int capacity) {
		super(capacity);
	}

	@Override
	public boolean offer(E e) {
		// override to put objects at the front of the list
		return super.offerFirst(e);
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {
		// override to put objects at the front of the list
		return super.offerFirst(e, timeout, unit);
	}

	@Override
	public boolean add(E e) {
		// override to put objects at the front of the list
		return super.offerFirst(e);
	}

	@Override
	public void put(E e) throws InterruptedException {
		// override to put objects at the front of the list
		super.putFirst(e);
	}
}