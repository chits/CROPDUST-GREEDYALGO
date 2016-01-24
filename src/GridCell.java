import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
/*
 * Author: Chitrali Rai
 * Making the Grid panel for displaying the coordinates of the station
 */

public class GridCell extends JPanel {
	
	private final int GRID_VIEW_SCALING_FACTOR = 6;

    private int gridWidth, gridHeight;
    private int xScale, yScale;
    Dimension size;
    private Graphics g;
    private Image gridImage;
    
    public GridCell(int height, int width)
    {
        gridHeight = height;
        gridWidth = width;
        size = new Dimension(0, 0);
    }
    
    public void preparePaint()
    {
        if(! size.equals(getSize())) { 
            size = getSize();
            gridImage = this.createImage(size.width, size.height);
            g = gridImage.getGraphics();

            xScale = size.width / gridWidth;
            if(xScale < 1) {
                xScale = GRID_VIEW_SCALING_FACTOR;
            }
            yScale = size.height / gridHeight;
            if(yScale < 1) {
                yScale = GRID_VIEW_SCALING_FACTOR;
            }
        }
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                             gridHeight * GRID_VIEW_SCALING_FACTOR);
    }
    /*
     * Draws the Grid Image
     * (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g)
    {
        if(gridImage != null) {
            Dimension currentSize = getSize();
            if(size.equals(currentSize)) {
                g.drawImage(gridImage, 0, 0, null);
            }
            else {
                g.drawImage(gridImage, 0, 0, currentSize.width, currentSize.height, null);
            }
        }
    }
    /*
     * Draw the station with the given coordinate
     */
    public void drawMark(int x, int y, Color color)
    {
        g.setColor(color);
        g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
    }
    
    public void clearAll(){
    	g.clearRect(0, 0, getWidth(), getHeight());
    }
    /*
     * Drawing the line between two points in coordinate
     */
    public void drawLine(Point p1, Point p2, Color color)
    {
        g.setColor(color);
        g.drawLine(p1.getX() * xScale, p1.getY() * yScale, p2.getX() * xScale, p2.getY() * yScale);
    }

}
