/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ctrl;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import model.CurrentData;
import view.Canvas;
//import view.Canvas;

/**
 *
 * @author z3ss
 */
public class ML implements MouseListener, MouseMotionListener{
    
    private final Canvas c;
    private final CurrentData cd = CurrentData.getInstance();
    private Point mouseStart;
    private boolean mousePressed;
    private Point mouseEnd;
    private Point currentMouse;
    private boolean mouseDragged;
    
    public ML(Canvas c){
        this.c = c;
    }
    
      @Override
    public void mouseClicked(MouseEvent me)
    {
        //s
    }


    @Override
    public void mousePressed(MouseEvent me)
    {
        mouseStart = me.getPoint();
        mousePressed = true;
    }


    @Override
    public void mouseReleased(MouseEvent me)
    {
        mouseEnd = me.getPoint();
        Rectangle r = createRect(mouseStart, mouseEnd);
        System.out.println(r.x+" "+r.y);
        System.out.println(r.width+" "+r.height);
        cd.updateArea(r);
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
        //s
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        //s
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        currentMouse = e.getPoint();
        mouseDragged = true;
        e.consume();//Stops the event when not in use, makes program run faster
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        currentMouse = e.getPoint();
        mouseDragged = false;
        e.consume();//Stops the event when not in use, makes program run faster
    }
    
    private Rectangle createRect(Point start, Point end){
        return new Rectangle((int)((start.x)*c.getScale()), 
                (int)((start.y)*c.getScale()), 
                (int)((end.x-start.x)*c.getScale()), 
                (int)((end.y-start.y)*c.getScale()));
    }
}
