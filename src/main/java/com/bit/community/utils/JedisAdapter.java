package com.bit.community.utils;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class JedisAdapter implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
	private JedisPool pool;

	@Override
	public void afterPropertiesSet() throws Exception {
		pool = new JedisPool("redis://39.105.83.97:6379");
	}

	public Long sadd(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return (long) 0;
	}

	public Long srem(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.srem(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return (long) 0;
	}

	public Long scard(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			// 这儿忘记回收线程，导致redis每次查询8次就挂掉
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return (long) 0;
	}

	public boolean sismember(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	public List<String> brpop(int timeout, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.brpop(timeout, key);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public long lpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}

	public List<String> lrange(String key, int start, int end) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public Jedis getJedis() {
		return pool.getResource();
	}

	public Transaction multi(Jedis jedis) {
		try {
			return jedis.multi();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public List<Object> exec(Transaction tx, Jedis jedis) {
		try {
			return tx.exec();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (tx != null) {
				tx.close();
			}
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public long zadd(String key, double score, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zadd(key, score, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return (long) 0;
	}

	public Set<String> zrevrange(String key, int start, int end) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public Long zcard(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zcard(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public Double zscore(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zscore(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}
}
