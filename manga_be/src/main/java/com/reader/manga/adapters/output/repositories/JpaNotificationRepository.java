package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.notifications.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n ORDER BY n.dataIn DESC")
    Page<Notification> findAllOrderByDataInDesc(Pageable pageable);

}
