package com.bit.community.utils;

public class RedisKeyUtil {
	private static String SPLIT = ":";
	private static String BIZ_LIKE = "LIKE";
	private static String BIZ_DISLIKE = "DISLIKE";
	private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
	private static String BIZ_FOLLOWER = "FOLLOWER";
	private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

	public static String getLikeKey(int entityType, int entityId) {
		return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getDisLikeKey(int entityType, int entityId) {
		return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getEventKey() {
		return BIZ_EVENTQUEUE;
	}

	public static String getFollowerKey(int entityType, int entityId) {
		return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	public static String getFolloweeKey(int userId, int entityId) {
		return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityId);
	}
	
    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
