package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

@Component
public class VikingListener {
    private VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public void addViking(Viking viking) {
        if (gui != null) {
            gui.addNewViking(viking);
        }
    }

    public void updateViking(Viking viking) {
        if (gui != null) {
            gui.updateViking(viking);
        }
    }

    public void deleteVikingById(int id) {
        if (gui != null) {
            gui.deleteVikingById(id);
        }
    }

    void testAdd() {
        addViking(service.createRandomViking());
    }
}

