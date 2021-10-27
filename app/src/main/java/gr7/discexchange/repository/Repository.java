package gr7.discexchange.repository;

import java.util.List;

import gr7.discexchange.model.Ad;
import gr7.discexchange.model.User;

public class Repository implements IRepository {

    @Override
    public List<Ad> getAllAds() {
        return null;
    }

    @Override
    public Ad getSpecificAd(String adUid) {
        return null;
    }

    @Override
    public List<Ad> getUserAds(String userUid) {
        return null;
    }

    @Override
    public void createAd(String userUid, Ad ad) {

    }

    @Override
    public void updateAd(String adUid, Ad ad) {

    }

    @Override
    public void deleteAd(String adUid) {

    }

    @Override
    public User getSpecificUser(String userUid) {
        return null;
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public void editUser(String userUid, User user) {

    }

    @Override
    public void deleteUser(String userUid) {

    }
}
