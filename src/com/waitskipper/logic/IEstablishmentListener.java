package com.waitskipper.logic;

import com.waitskipper.model.Establishment;

import java.util.ArrayList;

/**
 * Created by acheron0 on 2/7/2015.
 */
public interface IEstablishmentListener
{
    public void establishmentsChanged(ArrayList<Establishment> establishments);
}
