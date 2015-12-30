package com.vzome.core.exporters;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import com.vzome.core.algebra.AlgebraicField;
import com.vzome.core.algebra.AlgebraicNumber;
import com.vzome.core.model.Exporter;
import com.vzome.core.model.Manifestation;
import com.vzome.core.model.VefModelExporter;
import com.vzome.core.render.Colors;
import com.vzome.core.render.RenderedManifestation;
import com.vzome.core.render.RenderedModel;
import com.vzome.core.viewing.Lights;
import com.vzome.core.viewing.Camera;


public class VefExporter extends Exporter3d
{
	public VefExporter( Camera scene, Colors colors, Lights lights, RenderedModel model )
	{
	    super( scene, colors, lights, model );
	}

	public void doExport( File directory, Writer writer, int height, int width ) throws IOException
	{
	    AlgebraicField field = mModel .getField();
        AlgebraicNumber scale = field .createPower( -5 );
	    Exporter exporter = new VefModelExporter( writer, field, scale );
		
        for (RenderedManifestation rm : mModel) {
            Manifestation man = rm .getManifestation();
            exporter .exportManifestation( man );
        }
        
        exporter .finish();
	}

    public String getFileExtension()
    {
        return "vef";
    }
}


