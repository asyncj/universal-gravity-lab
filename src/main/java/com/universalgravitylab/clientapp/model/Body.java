package com.universalgravitylab.clientapp.model;

import javafx.scene.paint.Color;

import java.util.Arrays;

import static java.lang.Math.sqrt;

public class Body {

    private int numSteps;

    private String name;
    private double mass;
    private double radius;

    private boolean isStar;
    private boolean isStationary;

    private double[] rInit;
    private double[] vInit;
    private double[] aInit;

    private double[] r0;
    private double[] v0;
    private double[] a0;

    private Color color;

    // Define next conditions
    private double[] r1 = {0, 0, 0};        // initial position (m)
    private double[] v1 = {0, 0, 0};        // initial velocity (m/s)
    private double[] a1 = {0, 0, 0};               // initial acceleration (m/s^2)

    private double[][] r;
    private double[][] v;
    private double[][] a;

    public Body(String name, double mass, double radius, int numSteps, double[] r0, double[] v0, double[] a0, Color color, boolean isStar,
                boolean isStationary) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.numSteps = numSteps;
        this.rInit = Arrays.copyOf(r0, r0.length);
        this.vInit = Arrays.copyOf(v0, v0.length);
        this.aInit = Arrays.copyOf(a0, a0.length);
        this.r0 = r0;
        this.v0 = v0;
        this.a0 = a0;
        System.arraycopy(r0, 0, r1, 0, r1.length);
        System.arraycopy(v0, 0, v1, 0, v1.length);
        System.arraycopy(a0, 0, a1, 0, a1.length);
        this.color = color;
        this.r = new double[numSteps][3];
        this.v = new double[numSteps][3];
        this.a = new double[numSteps][3];
        this.isStar = isStar;
        this.isStationary = isStationary;
    }

    public double[] getR0() {
        return r0;
    }

    public double[] getV0() {
        return v0;
    }

    public double[] getA0() {
        return a0;
    }


    public double[] getR1() {
        return r1;
    }

    public double[] getV1() {
        return v1;
    }

    public double[] getA1() {
        return a1;
    }


    public void setR0(double[] r0) {
        this.r0 = r0;
    }

    public void setV0(double[] v0) {
        this.v0 = v0;
    }

    public void setA0(double[] a0) {
        this.a0 = a0;
    }

    public void setR1(double[] r1) {
        this.r1 = r1;
    }

    public void setV1(double[] v1) {
        this.v1 = v1;
    }

    public void setA1(double[] a1) {
        this.a1 = a1;
    }

    public void updateIndex(int i) {
        a[i][0] = a1[0];
        a[i][1] = a1[1];
        a[i][2] = a1[2];

        r[i][0] = r1[0];
        r[i][1] = r1[1];
        r[i][2] = r1[2];

        v[i][0] = v1[0];
        v[i][1] = v1[1];
        v[i][2] = v1[2];
    }

    public double getMass() {
        return mass;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public double[][] getR() {
        return r;
    }

    public double[][] getV() {
        return v;
    }

    public double[][] getA() {
        return a;
    }

    public double[] getRInit() {
        return rInit;
    }

    public double[] getVInit() {
        return vInit;
    }

    public double[] getAInit() {
        return aInit;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isStar() {
        return isStar;
    }

    public boolean isStationary() {
        return isStationary;
    }

    public double getVelocity(int pos) {
        return sqrt(v[pos][0] * v[pos][0] + v[pos][1] * v[pos][1] + v[pos][2] * v[pos][2]);
    }
}
