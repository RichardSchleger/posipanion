package sk.richardschleger.posipanion.comparators;

import java.util.Comparator;

import sk.richardschleger.posipanion.models.FriendModel;

public class FriendModelComparator implements Comparator<FriendModel>{

    @Override
    public int compare(FriendModel friend1, FriendModel friend2) {
        return (friend1.getSurname() + " " + friend1.getFirstName()).compareTo(friend2.getSurname() + " " + friend2.getFirstName());
    }
}
