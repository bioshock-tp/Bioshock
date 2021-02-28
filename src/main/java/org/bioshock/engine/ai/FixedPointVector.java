package org.bioshock.engine.ai;

import org.decimal4j.immutable.*;

public class FixedPointVector {

    Decimal5f x, y;

    private Decimal5f lerpAux(Decimal5f dt, Decimal5f xs, Decimal5f xf){
        return (xs.multiply(Decimal5f.valueOf(1).subtract(dt))).add(xf.multiply(dt));
    }

    public FixedPointVector(double x, double y) {
        this.x = Decimal5f.valueOf(x);
        this.y = Decimal5f.valueOf(y);
    }

    public FixedPointVector(Decimal5f x, Decimal5f y) {
        this.x = x;
        this.y = y;
    }

    public Decimal5f getX(){
        return this.x;
    }

    public Decimal5f getY(){

        return this.y;
    }

    public double getXDouble(){

        return (this.x).doubleValue();
    }

    public double getYDouble(){

        return (this.x).doubleValue();
    }

    public Decimal5f length(){
        return (((this.x).square()).addSquared(this.y)).sqrt();
    }

    public double lengthDouble(){
        return ((((this.x).square()).addSquared(this.y)).sqrt()).doubleValue();
    }

    public FixedPointVector rightVector(){
        return (new FixedPointVector(Decimal5f.valueOf(1), Decimal5f.valueOf(0)));
    }

    public FixedPointVector leftVector(){
        return (new FixedPointVector(Decimal5f.valueOf(-1), Decimal5f.valueOf(0)));
    }

    public FixedPointVector upVector(){
        return (new FixedPointVector(Decimal5f.valueOf(0), Decimal5f.valueOf(-1)));
    }

    public FixedPointVector downVector(){
        return (new FixedPointVector(Decimal5f.valueOf(0), Decimal5f.valueOf(1)));
    }

    public FixedPointVector zeroVector(){
        return (new FixedPointVector(Decimal5f.valueOf(0), Decimal5f.valueOf(0)));
    }

    public FixedPointVector oneVector(){
        return (new FixedPointVector(Decimal5f.valueOf(1), Decimal5f.valueOf(1)));
    }

    public FixedPointVector add(FixedPointVector v){
        return (new FixedPointVector((this.x).add(v.x), (this.y).add(v.y)));
    }

    public FixedPointVector subtract(FixedPointVector v){
        return (new FixedPointVector((this.x).subtract(v.x), (this.y).subtract(v.y)));
    }

    public FixedPointVector lerp(FixedPointVector v, Decimal5f dt){
        return (new FixedPointVector(lerpAux(dt, this.x, v.x), lerpAux(dt, this.y, v.y)));
    }

    public FixedPointVector normalized(){
        Decimal5f length = this.length();
        if(length.equals(Decimal5f.valueOf(0))){
            return zeroVector();
        }
        else{
            return (new FixedPointVector((this.x).divide(length), (this.y).divide(length)));
        }
    }

    public FixedPointVector normalizedPrime(){
        // The same as normalized, but returns a normalized vector only if length > 1, else return the vector
        Decimal5f length = this.length();
        if(length.isLessThanOrEqualTo(Decimal5f.valueOf(1))){
            return this;
        }
        else{
            return this.normalized();
        }
    }

    public Decimal5f distanceTo(FixedPointVector v){
        return (v.subtract(this)).length();
    }

    public double distanceToDouble(FixedPointVector v){
        return ((v.subtract(this)).length()).doubleValue();
    }

    public FixedPointVector directionTo(FixedPointVector v){
        return (v.subtract(this)).normalized();
    }

    public FixedPointVector directionToPrime(FixedPointVector v){
        // Same as directionTo, but applies normalizedPrime instead of normalized
        return (v.subtract(this)).normalizedPrime();
    }

    public FixedPointVector scaled(Decimal5f s){
        return (new FixedPointVector((this.x).multiply(s),(this.y).multiply(s)));
    }

    public Decimal5f dot(FixedPointVector v){
        return ((this.x).multiply(v.x)).add((this.y).multiply(v.y));
    }

}
