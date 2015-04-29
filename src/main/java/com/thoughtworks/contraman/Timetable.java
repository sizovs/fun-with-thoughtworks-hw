package com.thoughtworks.contraman;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

class Timetable {

    private final List<Track> tracks = new LinkedList<>();

    public Timetable(TimeConstraints constraints, Talks talks) {
        TalkConsumer talkConsumer = new TalkConsumer(talks.longestFirst());
        buildNewTrack(constraints, talkConsumer);
    }

    private void buildNewTrack(TimeConstraints constraints, TalkConsumer talks) {

        addTrack(constraints, timeslot -> {
            while ((timeslot.hasGaps() && talks.hasMoreTalks()) || talks.hasTalkThatFits(timeslot)) {
                Talk talkThatFits = talks.consumeOneThatFits(timeslot);
                timeslot.occupy(talkThatFits);
            }
        });

        if (talks.hasMoreTalks()) {
            buildNewTrack(constraints, talks);
        }
    }


    private void addTrack(TimeConstraints constraints, Consumer<Timeslot> timeslotOccupier) {
        Track track = new Track(constraints, timeslotOccupier);
        tracks.add(track);
    }

    public Collection<Event> events(int trackNo) {
        return tracks.get(trackNo).events();
    }

    public int trackCount() {
        return tracks.size();
    }

}
