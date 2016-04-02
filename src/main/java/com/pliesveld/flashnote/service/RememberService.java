package com.pliesveld.flashnote.service;

import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface RememberService extends PersistentTokenRepository {
}
