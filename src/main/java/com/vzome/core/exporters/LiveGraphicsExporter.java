package com.vzome.core.exporters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.vecmath.Vector3d;

import com.vzome.core.algebra.AlgebraicMatrix;
import com.vzome.core.algebra.AlgebraicVector;
import com.vzome.core.math.Polyhedron;
import com.vzome.core.math.RealVector;
import com.vzome.core.render.Color;
import com.vzome.core.render.Colors;
import com.vzome.core.render.RenderedManifestation;
import com.vzome.core.render.RenderedModel;
import com.vzome.core.viewing.Lights;
import com.vzome.core.viewing.ViewModel;

/**
 * Renders out to POV-Ray using #declare statements to reuse geometry.
 * @author vorth
 */
public class LiveGraphicsExporter extends Exporter3d
{
	private static final NumberFormat FORMAT = NumberFormat .getNumberInstance( Locale .US );
	
	
	public LiveGraphicsExporter( ViewModel scene, Colors colors, Lights lights, RenderedModel model )
	{
	    super( scene, colors, lights, model );
	}


	public void doExport( File directory, Writer writer, int height, int width ) throws IOException
	{
	    output = new PrintWriter( writer );
	    
		Vector3d lookDir = new Vector3d(), upDir = new Vector3d();
		mScene .getViewOrientation( lookDir, upDir );
		FORMAT .setMaximumFractionDigits( 6 );

        output .println( "Graphics3D[{" );
		
		FORMAT .setMaximumFractionDigits( 3 );
		
		for ( Iterator rms = mModel .getRenderedManifestations(); rms .hasNext(); )
		{
            output .print( "{FaceForm[" );
            RenderedManifestation rm = (RenderedManifestation) rms .next();
            printColor( rm .getColor() );
            output .println( "]," );
            
		    Polyhedron poly = rm .getShape();
		    boolean reverseFaces = rm .reverseOrder(); // need to reverse face vertex order
		    AlgebraicMatrix transform = rm .getOrientation();
            AlgebraicVector rmLoc = rm .getManifestation() .getLocation();

            List vertices = poly .getVertexList();
            output .println( "{" );
            for ( Iterator faces = poly .getFaceSet() .iterator(); faces .hasNext(); ){
                Polyhedron.Face face = (Polyhedron.Face) faces .next();
                int arity = face .size();
                output .print( "Polygon[{" );
                
                for ( int j = 0; j < arity; j++ ){
                    if ( j > 0 )
                        output .print( ", " );
                    Integer index = (Integer) face .get( reverseFaces? arity-j-1 : j );
                    AlgebraicVector loc = (AlgebraicVector) vertices .get( index .intValue() );
                    
                    // TODO need a unit test... don't know if the transform should be right or left
                    //   (migrated to rational vectors, but not tested)
                    loc = transform .timesColumn( loc ) .plus( rmLoc );
                    RealVector rv = loc .toRealVector();
                    output .print( "{" );
                    output .print( FORMAT.format( rv .x ) + ", " );
                    output .print( FORMAT.format( rv .y ) + ", " );
                    output .print( FORMAT.format( rv .z ) );
                    output .print( "}" );
                }
                output .print( "}]" );
                if ( faces .hasNext() )
                    output .println( "," );
                else
                    output .println();
            }
            output .print( "}}" );
            if ( rms .hasNext() )
                output .println( "," );
            else
                output .println();
            output .flush();
		}

		output .println( "},{Boxed -> False, Lighting -> False, DefaultColor -> GrayLevel[0]}]" );
		output .flush();
	}

    
    private void printColor( Color color )
    {
		output .print( "RGBColor[" );
		float[] rgb = color .getRGBColorComponents( new float[3] );
		output .print( FORMAT.format(rgb[0]) + ", " );
		output .print( FORMAT.format(rgb[1]) + ", " );
		output .print( FORMAT.format(rgb[2]) );
		output .print( "]" );
    }


    public String getFileExtension()
    {
        return "m";
    }

}


