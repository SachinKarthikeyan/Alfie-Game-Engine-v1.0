package com.sachin.engine;

import com.sachin.engine.gfx.Font;
import com.sachin.engine.gfx.Image;
import com.sachin.engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;

public class Renderer
{
    private int pW, pH;
    private int[] p;
    private int[] zb;
    private int zDepth = 0;
    private Font font = Font.STANDARD;

    public Renderer(GameContainer gc)
    {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData( );
        zb = new int[p.length];
    }
    public void clear()
    {
        for(int i =0 ;i < p.length;i++)
        {
            p[i] = 0  ;
            zb[i] = 0;
        }
    }
    public  void setPixel(int x,int y ,int value)
    {
        int alpha = ((value >> 24) & 0xff);
        if(x < 0 || x >= pW || y < 0 || y >= pH || alpha == 0  ) {return;}

        if(zb[x +y *pW] > zDepth){return;}
        if(alpha == 255){
            p[x+y *pW] = value;
        }
        else{
            int pixelColor = p[x+y *pW];

            int newRed = (pixelColor >> 16) & 0xff +(int) (((pixelColor >> 16) & 0xff - (value >> 16) & 0xff) * alpha/255f);
            int newGreen= (pixelColor >> 8) & 0xff +(int) (((pixelColor >> 8) & 0xff - (value >> 16) & 0xff) * alpha/255f);
            int newBlue= pixelColor & 0xff +(int) ((pixelColor & 0xff) - (value  & 0xff) * (alpha/255f));

            p[x+y *pW] = (255 << 24 | newRed << 16| newGreen << 8 | newBlue);
        }
        p[x + y * pW] = value;
    }
    public void drawText(String text, int offX, int offY, int color)
    {
        text = text.toUpperCase();
        int offset = 0;


        for(int i=0;i< text.length();i++)
        {
            int unicode = text.codePointAt(i) - 32;

            for(int y = 0; y < font.getFontImage().getH();y++)
            {
                for(int x = 0;x < font.getWidths()[unicode];x++)
                {
                    if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y *font.getFontImage().getW()] == 0xffffffff)
                    {
                         setPixel(x+ offX +offset,y + offY,color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }
    public void drawImage(Image image, int offX, int offY)
    {
        //CODE TO MAKE THE IMAGE NOT RENDER
        if(offX < -image.getW()) return;
        if(offY < -image.getH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX =0;
        int newY =0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // CODE THAT CLIPS THE IMAGE WHEN OUT OF BOUNDS
        if(offX< 0) {newX -= offX;}
        if(offY< 0) {newY -= offY;}
        if(newWidth + offX >= pW) {newWidth -= newWidth + offX - pW;}
        if(newHeight + offY >= pH) {newHeight -= newHeight + offY - pH;}

        for(int y=newY;y < newHeight ;y++)
        {
            for(int x = newY;x < newWidth; x++)
            {
                    setPixel(x + offX,y + offY,image.getP()[x + y * image.getW() ]);
            }
        }
    }
    public void drawImageTile(ImageTile image,int offX, int offY, int tileX, int tileY)
    {

        //CODE TO MAKE THE IMAGE NOT RENDER
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX =0;
        int newY =0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        // CODE THAT CLIPS THE IMAGE WHEN OUT OF BOUNDS
        if(offX< 0) {newX -= offX;}
        if(offY< 0) {newY -= offY;}
        if(newWidth + offX >= pW) {newWidth -= newWidth + offX - pW;}
        if(newHeight + offY >= pH) {newHeight -= newHeight + offY - pH;}

        for(int y=newY;y < newHeight ;y++)
        {
            for(int x = newY;x < newWidth; x++)
            {
                setPixel(x + offX,y + offY,image.getP()[(x+ tileX * image.getTileW())+ (y +tileY * image.getTileW()) * image.getW() ]);
            }
        }
    }
    public void drawRect(int offX,int offY ,int width, int height,int color)
    {



        for(int y=0;y <= height; y++)
        {
            setPixel(offX, y+ offY,color);
            setPixel(offX + width, y+ offY,color);
        }
        for(int x=0;x <=width ; x++)
        {
            setPixel(x + offX, offY,color);
            setPixel(x + offX , offY + height ,color);
        }
    }
    public void drawFillRect(int offX,int offY ,int width, int height,int color)
    {
        //CODE TO MAKE THE IMAGE NOT RENDER
        if(offX < -width) return;
        if(offY < -height) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX =0;
        int newY =0;
        int newWidth = width;
        int newHeight =height;

        // CODE THAT CLIPS THE IMAGE WHEN OUT OF BOUNDS
        if(offX< 0) {newX -= offX;}
        if(offY< 0) {newY -= offY;}
        if(newWidth + offX >= pW) {newWidth -= newWidth + offX - pW;}
        if(newHeight + offY >= pH) {newHeight -= newHeight + offY - pH;}

        for(int y=newY;y <= newHeight; y++)
        {
            for(int x=newX;x <= newWidth; x++)
            {
                setPixel(x + offX,y+ offY,color);
            }
        }

    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
