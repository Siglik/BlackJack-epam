package org.qqq175.blackjack.pool;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.atomic.AtomicReference;

import org.qqq175.blackjack.game.BJGame;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class GamePool {
	private static final long serialVersionUID = 1L;
	private static AtomicReference<GamePool> instance = new AtomicReference<>();
	private ConcurrentHashMap<UserId, BJGame> users;
	private static Semaphore lock = new Semaphore(1);
	private static boolean isEmpty = true;

	private GamePool() {
		users = new ConcurrentHashMap<>();
	}

	public static GamePool getInstance() {
		if (isEmpty) {
			try {
				lock.acquire();
			} catch (InterruptedException e) {
				// TODO log
			}
			if (instance.get() == null) {
				instance.set(new GamePool());
				isEmpty = false;
			}
			lock.release();
		}
		return instance.get();
	}
}
