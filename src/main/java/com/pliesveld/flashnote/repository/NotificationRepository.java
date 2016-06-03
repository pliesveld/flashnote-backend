package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Notification;
import com.pliesveld.flashnote.domain.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.stream.Stream;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification,Integer> {

    @Modifying
    @Query("delete from Notification n where n.recipient <= ?1")
    void deleteAllByRecipient(Student recipient);

    @Modifying
    @Query("delete from Notification n where n.createdOn <= ?1")
    void deleteAllByCreatedOnLessThan(Instant now);

    Stream<Notification> findByRecipientOrderByCreatedOnAsc(Student recipient);

    Stream<Notification> findByRecipientAndCreatedOnLessThanOrderByCreatedOnAsc(Student recipient, Instant last_login);
}
