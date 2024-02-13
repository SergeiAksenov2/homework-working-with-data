package com.sample.carmarket.screen.manufacturer;

import com.sample.carmarket.entity.EngineType;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import com.sample.carmarket.entity.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@UiController("Manufacturer.browse")
@UiDescriptor("manufacturer-browse.xml")
@LookupComponent("table")
public class ManufacturerBrowse extends MasterDetailScreen<Manufacturer> {

    @Autowired
    private Table<Manufacturer> table;

    @Autowired
    private Notifications notifications;

    @Autowired
    private DataManager dataManager;

    @Subscribe("table.calculateCars")
    public void onTableCalculateCars(final Action.ActionPerformedEvent event) {
        Manufacturer selectedManufacturer = table.getSingleSelected();
        if (selectedManufacturer != null) {
            UUID selectedManufacturerId = selectedManufacturer.getId();
            int electricCars = dataManager.loadValue("select count(c) from Car c " +
                            "where c.model.manufacturer.id = :modelManufacturerId1 " +
                            "and c.model.engineType = :modelEngineType1", Integer.class)
                    .parameter("modelEngineType1", EngineType.ELECTRIC)
                    .parameter("modelManufacturerId1", selectedManufacturerId)
                    .one();
            int gasolineCars = dataManager.loadValue("select count(c) from Car c " +
                            "where c.model.manufacturer.id = :modelManufacturerId1 " +
                            "and c.model.engineType = :modelEngineType1", Integer.class)
                    .parameter("modelEngineType1", EngineType.GASOLINE)
                    .parameter("modelManufacturerId1", selectedManufacturerId)
                    .one();
            notifications.create()
                    .withType(Notifications.NotificationType.TRAY)
                    .withDescription("Electric cars: " + electricCars + ", gasoline cars: " + gasolineCars)
                    .show();

        } else {
            notifications.create()
                    .withType(Notifications.NotificationType.TRAY)
                    .withDescription("The manufacturer is not selected")
                    .show();
        }
    }
}