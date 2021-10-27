package gr7.discexchange.repository;

import java.util.List;

import gr7.discexchange.model.Ad;
import gr7.discexchange.model.User;

public interface IRepository {

    // Ad

    List<Ad> getAllAds();
    Ad getSpecificAd(String adUid);
    List<Ad> getUserAds(String userUid);

    void createAd(String userUid, Ad ad);
    void updateAd(String adUid, Ad ad);
    void deleteAd(String adUid);

    // User

    User getSpecificUser(String userUid);

    void createUser(User user);
    void editUser(String userUid, User user);
    void deleteUser(String userUid);



}
