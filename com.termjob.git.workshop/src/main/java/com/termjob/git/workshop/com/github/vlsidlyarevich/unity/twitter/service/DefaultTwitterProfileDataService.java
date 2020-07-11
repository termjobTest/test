package com.github.vlsidlyarevich.unity.twitter.service;

import com.github.vlsidlyarevich.unity.domain.service.TwitterProfileService;
import com.github.vlsidlyarevich.unity.twitter.exception.TwitterServiceException;
import com.github.vlsidlyarevich.unity.twitter.model.TwitterPopularProfile;
import com.github.vlsidlyarevich.unity.twitter.model.TwitterProfileData;
import com.github.vlsidlyarevich.unity.twitter.model.TwitterSubscriptionData;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTwitterProfileDataService implements TwitterProfileDataService {

    private static final int IDS_SUB_ARRAY_START_INDEX = 0;
    private static final int IDS_SUB_ARRAY_END_INDEX_ARGUMENT = 100;
    private static final Long CURSOR = -1L;

    private final Mapper mapper;
    private final Twitter twitter;
    private final TwitterProfileService twitterProfileService;
    private final TwitterProfileDataCalculator tagsCalculator;

    @Override
    public Optional<TwitterProfileData> getData(final String username) {
        return Optional.ofNullable(username)
                .map(usrname -> {
                    final TwitterProfileData profileData = getTwitterUser(username);

                    final List<TwitterSubscriptionData> subscriptions =
                            subscriptions(username)
                                    .map(subscr -> mapper.map(subscr, TwitterSubscriptionData.class))
                                    .collect(Collectors.toList());

                    profileData.setPopularProfilesSubscribed(getPopularProfiles(subscriptions));

                    profileData.setTagsTotal(tagsCalculator.calculateTotal(profileData.getPopularProfilesSubscribed()));

                    return Optional.of(profileData);
                }).orElseThrow(() -> new IllegalArgumentException("Twitter username should not be empty"));
    }

    private TwitterProfileData getTwitterUser(final String username) {
        try {
            return mapper.map(twitter.users().showUser(username), TwitterProfileData.class);
        } catch (final TwitterException e) {
            throw new TwitterServiceException(e);
        }
    }

    private Stream<User> subscriptions(final String username) {
        try {
            final IDs subscriptionsIDs = twitter.getFriendsIDs(username, CURSOR);
            return lookupUsers(subscriptionsIDs.getIDs()).stream();
        } catch (final TwitterException e) {
            throw new TwitterServiceException(e);
        }
    }

    private List<User> lookupUsers(final long[] ids) throws TwitterException {
        boolean lookup = true;
        int position = IDS_SUB_ARRAY_START_INDEX;

        final List<User> result = new ArrayList<>();
        while (lookup) {
            long[] idsPack = ArrayUtils.subarray(ids, position, position + IDS_SUB_ARRAY_END_INDEX_ARGUMENT);

            if (idsPack.length == 0) {
                lookup = false;
            } else {
                result.addAll(twitter.lookupUsers(idsPack));
                position += IDS_SUB_ARRAY_END_INDEX_ARGUMENT;
            }
        }

        return result;
    }

    private List<TwitterPopularProfile> getPopularProfiles(final List<TwitterSubscriptionData> subscriptions) {
        final List<TwitterPopularProfile> popularProfiles = new ArrayList<>();

        subscriptions.forEach(subscription ->
                Optional.ofNullable(twitterProfileService.findByScreenName(subscription.getScreenName()))
                        .ifPresent(popularProfile -> {
                            final TwitterPopularProfile profile
                                    = mapper.map(popularProfile, TwitterPopularProfile.class);

                            popularProfiles.add(profile);
                        }));

        return popularProfiles;
    }
}
