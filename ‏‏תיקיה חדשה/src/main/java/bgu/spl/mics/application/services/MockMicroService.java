package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

public class MockMicroService extends MicroService {

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public MockMicroService() {
        super("Mock");
    }

    @Override
    protected void initialize() {

    }
}
