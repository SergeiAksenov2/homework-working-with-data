package com.sample.carmarket.screen.car;

import com.sample.carmarket.entity.Status;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import com.sample.carmarket.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@UiController("Car.browse")
@UiDescriptor("car-browse.xml")
@LookupComponent("carsTable")
public class CarBrowse extends StandardLookup<Car> {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private GroupTable<Car> carsTable;

    @Autowired
    private Notifications notifications;

    @Subscribe("carsTable.carMarkAsSold")
    public void onCarsTableCarMarkAsSold(final Action.ActionPerformedEvent event) {
        Car carSelected = carsTable.getSingleSelected();
        if (carSelected != null) {
            if (carSelected.getStatus() == Status.SOLD) {
                notifications.create()
                        .withType(Notifications.NotificationType.TRAY)
                        .withDescription("Already Sold")
                        .show();
            } else {
                carSelected.setStatus(Status.SOLD);
                carSelected.setDateOfSale(LocalDate.now());
                dataManager.save(carSelected);
                notifications.create()
                        .withType(Notifications.NotificationType.TRAY)
                        .withDescription("Done")
                        .show();
            }
        }
    }
}