package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, UUID> {
}
