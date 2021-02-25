package org.bioshock.engine.ai;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.physics.Movement;
import org.bioshock.engine.renderers.SwatterRenderer;

public class Swatter extends SquareEntity {


    public Movement movement;
    public Enemy enemy;

    public boolean shouldSwat = false;
    private boolean swatBack = false;

    protected int angles = 0;


    public Swatter(Point3D pos,Size s, Color c, Enemy enemy) {
        super(pos, s, 0, c);

        renderer = new SwatterRenderer();

        this.enemy = enemy;

    }

    public void setEnemy(Enemy e){
        enemy = e;
    }

    public Enemy getEnemy(){
        return enemy;
    }

    private void swat(){
        int s = 4;
        if(angles < 180/s){
            setRotation(new Rotate(getRotation().getAngle() + s, getRotation().getPivotX(), getRotation().getPivotY()));
            angles++;
        }
        else {
            angles = 180;
            swatBack = true;
        }
    }

    private void swatB(){
        int s = 4;
        if(angles > 0){
            setRotation(new Rotate(getRotation().getAngle() - s, getRotation().getPivotX(), getRotation().getPivotY()));
            angles -= s;
        }
        else{
            angles = 0;
            shouldSwat = false;
            swatBack = false;
        }
    }


    @Override
    protected void tick(double timeDelta) {
        if(shouldSwat){
            if(swatBack){
                swatB();
            }
            else{
                swat();
            }
        }
    }
}
