package com.bit.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.community.model.Feed;
import com.bit.community.dao.FeedDao;

@Service
public class FeedService {
    @Autowired
    FeedDao feedDao;

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        feedDao.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id) {
        return feedDao.getFeedById(id);
    }
}
