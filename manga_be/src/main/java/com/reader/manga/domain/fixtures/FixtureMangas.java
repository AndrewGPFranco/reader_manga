package com.reader.manga.domain.fixtures;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FixtureMangas {

    public static Set<String> getMangasDefault() {
        return new HashSet<>(Arrays.asList(
                "Demon Slayer", "One Piece", "Noragami", "Monster",
                "Ranking of Kings", "One Punch Man", "The Rising Of The Shield Hero",
                "Boruto Two Blue Vortex", "Berserk", "Tokyo Revengers",
                "Record of Ragnarok", "Dan da dan", "The Promise Neverland",
                "Mob Psycho 100", "Hunter x Hunter", "Akame Ga Kill",
                "Spy x Family", "Atack on Titan", "Bungou Stray Dogs",
                "Jojo's Adventure Bizarre", "Hellsing", "My Hero Academia"
        ));
    }

}
