package edu.hsb.proto.test.service;

public interface ILoginService {
    boolean login(String username, String passwordHash, int rounds, Boolean encryption);
    String hash(String stringToHash, int rounds, Boolean encryption);
}