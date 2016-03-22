package com.pliesveld.flashnote.service;

public interface RateLimitService {
    void recordRemoteAccess(String ipAddr);
    boolean isBlocked(String ipAddr);
}
