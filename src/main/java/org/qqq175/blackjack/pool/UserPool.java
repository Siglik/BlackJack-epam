package org.qqq175.blackjack.pool;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Pool for managing online users
 * 
 * @author qqq175
 */
public class UserPool implements Serializable {
	private static final String UNEXPECTED_INTERRUPT = "Unexpected interrupt";
	private static Logger log = LogManager.getLogger(UserPool.class);
	private static final long serialVersionUID = 1L;
	private static AtomicReference<UserPool> instance = new AtomicReference<>();
	private ConcurrentHashMap<UserId, User> users;
	private static Semaphore lock = new Semaphore(1);
	private static boolean isEmpty = true;

	private UserPool() {
		users = new ConcurrentHashMap<>();
	}

	public static UserPool getInstance() {
		if (isEmpty) {
			try {
				lock.acquire();
			} catch (InterruptedException e) {
				log.error(UNEXPECTED_INTERRUPT, e);
			}
			if (instance.get() == null) {
				instance.set(new UserPool());
				isEmpty = false;
			}
			lock.release();
		}
		return instance.get();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#containsKey(java.lang.Object)
	 */
	public boolean containsKey(UserId key) {
		return users.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#containsValue(java.lang.Object)
	 */
	public boolean containsValue(User value) {
		return users.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#entrySet()
	 */
	public Set<Entry<UserId, User>> entrySet() {
		return users.entrySet();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#get(java.lang.Object)
	 */
	public User get(UserId key) {
		return users.get(key);
	}

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#keySet()
	 */
	public KeySetView<UserId, User> keySet() {
		return users.keySet();
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#put(java.lang.Object,
	 *      java.lang.Object)
	 */
	public User put(User value) {
		return users.put(value.getId(), value);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#replace(java.lang.Object,
	 *      java.lang.Object)
	 */
	public User replace(User value) {
		return users.replace(value.getId(), value);
	}

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#size()
	 */
	public int size() {
		return users.size();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#values()
	 */
	public Collection<User> values() {
		return users.values();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.concurrent.ConcurrentHashMap#remove(java.lang.Object)
	 */
	public User remove(UserId key) {
		return users.remove(key);
	}

}
