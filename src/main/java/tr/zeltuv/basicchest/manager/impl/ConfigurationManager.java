package tr.zeltuv.basicchest.manager.impl;

import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.configuration.Configuration;
import tr.zeltuv.basicchest.manager.IManager;

public class ConfigurationManager implements IManager {

    @Override
    public void load(ChestPlugin plugin) {
        for (Configuration value : Configuration.values()) {
            value.initializeConfiguration();
        }
    }

    @Override
    public void unload() {

    }
}