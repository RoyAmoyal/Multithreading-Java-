package bgu.spl.mics.application.messages;

import bgu.spl.mics.MicroService;

public class MockMicroService extends MicroService {

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public MockMicroService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

    }
}
