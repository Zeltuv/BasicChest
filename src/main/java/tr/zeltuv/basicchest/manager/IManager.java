package tr.zeltuv.basicchest.manager;

import tr.zeltuv.basicchest.ChestPlugin;

public interface IManager {

    void load(ChestPlugin plugin);

    void unload();
}
