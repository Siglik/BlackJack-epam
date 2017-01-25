package org.qqq175.blackjack.pool;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.qqq175.blackjack.game.BJGame;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class GamePool implements Serializable {
	private static final long serialVersionUID = 1L;
	private static AtomicReference<GamePool> instance = new AtomicReference<>();
	private ConcurrentHashMap<UserId, BlackJackGame> games;
	private static Semaphore lock = new Semaphore(1);
	private static boolean isEmpty = true;

	private GamePool() {
		games = new ConcurrentHashMap<UserId, BlackJackGame>();
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

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#size()
	 */
	public int size() {
		return games.size();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#isEmpty()
	 */
	public boolean isEmpty() {
		return games.isEmpty();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#get(java.lang.Object)
	 */
	public BlackJackGame get(UserId key) {
		return games.get(key);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#containsKey(java.lang.Object)
	 */
	public boolean containsKey(UserId key) {
		return games.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#containsValue(java.lang.Object)
	 */
	public boolean containsValue(BlackJackGame value) {
		return games.containsValue(value);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#remove(java.lang.Object)
	 */
	public BlackJackGame remove(UserId key) {
		return games.remove(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#replace(java.lang.Object,
	 *      java.lang.Object)
	 */
	public BlackJackGame replace(UserId key, BlackJackGame value) {
		return games.replace(key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#put(java.lang.Object,
	 *      java.lang.Object)
	 */
	public BlackJackGame put(UserId key, BlackJackGame value) {
		return games.put(key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#putIfAbsent(java.lang.Object,
	 *      java.lang.Object)
	 */
	public BlackJackGame putIfAbsent(UserId key, BlackJackGame value) {
		return games.putIfAbsent(key, value);
	}
}
