/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.esens.espdvcd.builder.schema;

public class SchemaFactory {
    
    public static final ESPDRequestSchemaExtractor ESPD_REQUEST = new ESPDRequestSchemaExtractor();
    public static final ESPDResponseSchemaExtractor ESPD_RESPONSE = new ESPDResponseSchemaExtractor();
    public static final VCDResponseSchemaExtractor VCD_RESPONSE = new VCDResponseSchemaExtractor();

}
