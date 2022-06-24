package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jinwon.rpm.profile.domain.terms.QTerms.terms;

/**
 * 약관 RepositoryImpl
 */
@Repository
public class TermsRepositoryImpl implements TermsCustomRepository {

    public static final int LIMIT_SIZE = 1;
    private final JPAQueryFactory jpaQueryFactory;

    public TermsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Terms findUseTermsByType(TermsType type) {
        return jpaQueryFactory.selectFrom(terms)
                .where(terms.type.eq(type)
                        .and(terms.useType.eq(UseType.USE)))
                .orderBy(terms.termsId.desc())
                .limit(LIMIT_SIZE)
                .fetch()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_TERMS));
    }

    @Override
    public List<Terms> findListUseTerms(List<TermsType> termsTypes) {
        return jpaQueryFactory.from(terms)
                .where(terms.type.in(termsTypes)
                        .and(terms.useType.eq(UseType.USE)))
                .orderBy(terms.termsId.desc())
                .transform(
                        GroupBy.groupBy(terms.type)
                                .list(terms));
    }

}