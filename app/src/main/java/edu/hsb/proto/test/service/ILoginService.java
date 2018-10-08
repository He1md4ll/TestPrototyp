package edu.hsb.proto.test.service;

public interface ILoginService {
    boolean loginOnline(String username, String passwordHash);
    boolean loginOffline(String username, String passwordHash, int rounds, Boolean encryption);
    String hash(String stringToHash, int rounds, Boolean encryption);
}