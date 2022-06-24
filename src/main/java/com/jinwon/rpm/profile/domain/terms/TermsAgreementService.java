package com.jinwon.rpm.profile.domain.terms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService {

    private final TermsRepository termsRepository;
    private final TermsRepositoryImpl termsRepositoryImpl;


}

