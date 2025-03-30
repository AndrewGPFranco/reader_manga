package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.UserChapter;

public interface UserChapterRepository {

    UserChapter save(UserChapter userChapter);

}