package com.model.Automobile;
import com.DAO.*;
import com.model.Fines.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class MotorVehicleRegistry {
    private static final String name = "Mar del Plata Car Dealership";
    private final Set<Brand> brands;
    private final Map<Automobile,List<Fine>> automobilesInformation;
    private static MotorVehicleRegistry instance = null;

    private MotorVehicleRegistry(){
        this.brands = new TreeSet<>();
        this.automobilesInformation = new TreeMap<Automobile,List<Fine>>();
    }

    public static MotorVehicleRegistry getMotorVehicleRegistry(){
        if(instance == null)
            instance = new MotorVehicleRegistry();
        return instance;
    }

    public Automobile getRandomAutomobile(){
        AutomobileDAO automobileDao = new AutomobileDAO();
        Integer automobileId;
        Automobile a = null;
        try {
            automobileId = automobileDao.getAutomobileIdByLicensePlate("AB123CD");
            a = automobileDao.getAutomobileByAutomobileID(automobileId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            return a;
        }

    }

    public void addBrand(Brand brand) {
        BrandsDAO brandDao = new BrandsDAO();
        ModelsDAO modelDao = new ModelsDAO();
        try{
            Integer brandID = brandDao.insertBrand(brand.getName());
            for (Iterator it = brand.getModels(); it.hasNext(); ) {
                Model m = (Model) it.next();
                modelDao.insertModel(m.getName(), brandID);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void addAutomobile(Automobile a) {
        automobilesInformation.put(a,new ArrayList<>());
        AutomobileDAO automobileDao = new AutomobileDAO();
        ModelsDAO modelDao = new ModelsDAO();
        OwnersDAO ownerDao = new OwnersDAO();
        try{
            Integer modelId = modelDao.getModelIdByName(a.getModel().getName());
            Integer ownerId = ownerDao.getOwnerIdByLegalID(a.getOwner().getLegalIid());
            automobileDao.insertAutomobile(a.getLicensePlate(),a.getYear(),modelId,ownerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addFineToAutomobile(Automobile a, Fine f) { automobilesInformation.get(a).add(f); }


    public void showAllAutomobiles() {
        for (Automobile a : automobilesInformation.keySet()) {
            System.out.println(a);
        }
    }

    public int getScoring(Automobile a) {
        int scoring = 20;
        List<Fine> fines = automobilesInformation.get(a);
        for (Fine f : fines) {
            scoring-=f.getScoring();
        }
        return scoring;
    }

    public void informFines() {
        Iterator<Automobile> it = automobilesInformation.keySet().iterator();
        while(it.hasNext()) {
            Automobile actualAutomobile = it.next();
            System.out.format("THE %s has the next fines: \n",actualAutomobile.toString() );
            for(Fine f : automobilesInformation.get(actualAutomobile)){
                System.out.format("\t %s \n", f.toString());
            }
            System.out.println('\n');
        }
    }
}