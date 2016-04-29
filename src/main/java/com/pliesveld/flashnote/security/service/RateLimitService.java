package com.pliesveld.flashnote.security.service;

public interface RateLimitService {
    void recordRemoteAccess(String ipAddr);
    boolean isBlocked(String ipAddr);
}
