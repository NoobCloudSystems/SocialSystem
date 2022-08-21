package xyz.luccboy.socialsystem.config;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class Localization {

    // General
    private String noSelfRequest;
    private String noSelfInvite;
    private String neverOnlineBefore;
    private String notOnline;
    private String errorJump;
    private String disabledJump;
    private String disabledPartyInvites;
    private String alreadyConnected;
    private String willBeConnected;
    private String noMessage;

    // Friend
    private String noFriends;
    private String noFriendRequests;
    private String alreadyFriends;
    private String notFriendWith;
    private String noFriendRequestFrom;
    private String sentFriendRequest;
    private String receivedFriendRequest;
    private String noFriendAnymore;
    private String acceptFriendRequest;
    private String denyFriendRequest;
    private String hoverFriendAccept;
    private String hoverFriendDeny;
    private String friendListOnline;
    private String friendListOffline;
    private String isOnline;
    private String isOffline;
    private String friendListRequest;
    private String messageToFriend;
    private String messageFromFriend;
    private String startConversation;

    // Party
    private String noParty;
    private String partyNotExisting;
    private String noPartyInvitation;
    private String hoverPartyAccept;
    private String hoverPartyDeny;
    private String acceptPartyRequest;
    private String denyPartyRequest;
    private String sentPartyRequest;
    private String receivedPartyRequest;
    private String partyListLeader;
    private String partyListMembers;
    private String partyListMember;
    private String joinedParty;
    private String leftParty;
    private String newPartyLeader;

    // General
    public String getWillBeConnected(final String argument) {
        return MessageFormat.format(willBeConnected, argument);
    }

    // Friend
    public String getSentFriendRequest(final String argument) {
        return MessageFormat.format(sentFriendRequest, argument);
    }

    public String getReceivedFriendRequest(final String argument) {
        return MessageFormat.format(receivedFriendRequest, argument);
    }

    public String getNoFriendAnymore(final String argument) {
        return MessageFormat.format(noFriendAnymore, argument);
    }

    public String getAcceptFriendRequest(final String argument) {
        return MessageFormat.format(acceptFriendRequest, argument);
    }

    public String getDenyFriendRequest(final String argument) {
        return MessageFormat.format(denyFriendRequest, argument);
    }

    public String getFriendListOnline(final String... arguments) {
        return MessageFormat.format(friendListOnline, (Object[]) arguments);
    }

    public String getFriendListOffline(final String argument) {
        return MessageFormat.format(friendListOffline, argument);
    }

    public String getIsOnline(final String argument) {
        return MessageFormat.format(isOnline, argument);
    }

    public String getIsOffline(final String argument) {
        return MessageFormat.format(isOffline, argument);
    }

    public String getFriendListRequest(final String argument) {
        return MessageFormat.format(friendListRequest, argument);
    }

    public String getMessageToFriend(final String... arguments) {
        return MessageFormat.format(messageToFriend, (Object[]) arguments);
    }

    public String getMessageFromFriend(final String... arguments) {
        return MessageFormat.format(messageFromFriend, (Object[]) arguments);
    }


    // Party
    public String getAcceptPartyRequest(final String argument) {
        return MessageFormat.format(acceptPartyRequest, argument);
    }

    public String getDenyPartyRequest(final String argument) {
        return MessageFormat.format(denyPartyRequest, argument);
    }

    public String getSentPartyRequest(final String argument) {
        return MessageFormat.format(sentPartyRequest, argument);
    }

    public String getReceivedPartyRequest(final String argument) {
        return MessageFormat.format(receivedPartyRequest, argument);
    }

    public String getPartyListLeader(final String argument) {
        return MessageFormat.format(partyListLeader, argument);
    }

    public String getPartyListMembers(final String argument) {
        return MessageFormat.format(partyListMembers, argument);
    }

    public String getPartyListMember(final String argument) {
        return MessageFormat.format(partyListMember, argument);
    }

    public String getJoinedParty(final String argument) {
        return MessageFormat.format(joinedParty, argument);
    }

    public String getLeftParty(final String argument) {
        return MessageFormat.format(leftParty, argument);
    }

    public String getNewPartyLeader(final String argument) {
        return MessageFormat.format(newPartyLeader, argument);
    }

}
