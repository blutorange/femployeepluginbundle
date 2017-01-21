//package de.xima.fc.fuerth.employees;
//
//import java.io.File;
//import java.nio.charset.Charset;
//
//import org.apache.commons.io.FileUtils;
//
//import de.xima.fc.bl.fdv.processing.result.ProcessingResultStringValue;
//import de.xima.fc.entities.Attachment;
//import de.xima.fc.form.helper.AttachmentHelper;
//import de.xima.fc.interfaces.plugin.param.workflow.IPluginProcessingParams;
//import de.xima.fc.interfaces.plugin.retval.workflow.IPluginProcessingRetVal;
//import de.xima.fc.mdl.enums.EAttachmentSource;
//import de.xima.fc.plugin.exception.FCPluginException;
//import de.xima.fc.plugin.interfaces.workflow.IPluginProcessing;
//
//public class GimmeLogPlugin implements IPluginProcessing {
//
//	@Override
//	public String getName() {
//		return "GimmeLogPlugin";
//	}
//
//	@Override
//	public IPluginProcessingRetVal execute(final IPluginProcessingParams p) throws FCPluginException {
//		try {
//			//final String s1 = FileUtils.readFileToString(new File("/var/log/formcycle/formcycle.log"), Charset.defaultCharset());
//			//final String s2 = FileUtils.readFileToString(new File("/var/log/formcycle/plugins.log"), Charset.defaultCharset());
//			final String s3 = FileUtils.readFileToString(new File("/home/fc/tomcat7/logs/catalina.out"), Charset.defaultCharset());
//			//final Attachment attachment1 = AttachmentHelper.createAttachmentBody(s1.getBytes(), p.getWorkflowProcessingContext().getVorgang().getBearbeiter().getName(), "formcycle.log", p.getWorkflowProcessingContext().getVorgang().getMandant(), p.getWorkflowProcessingContext().getVorgang(), EAttachmentSource.WORKFLOW);
//			//final Attachment attachment2 = AttachmentHelper.createAttachmentBody(s2.getBytes(), p.getWorkflowProcessingContext().getVorgang().getBearbeiter().getName(), "plugin.log", p.getWorkflowProcessingContext().getVorgang().getMandant(), p.getWorkflowProcessingContext().getVorgang(), EAttachmentSource.WORKFLOW);
//			final Attachment attachment3 = AttachmentHelper.createAttachmentBody(s3.substring(s3.length()-3000000).getBytes(), p.getWorkflowProcessingContext().getVorgang().getBearbeiter().getName(), "catalina.out", p.getWorkflowProcessingContext().getVorgang().getMandant(), p.getWorkflowProcessingContext().getVorgang(), EAttachmentSource.WORKFLOW);
//			//p.getWorkflowProcessingContext().getVorgang().addAttachment(attachment1);
//			//p.getWorkflowProcessingContext().getVorgang().addAttachment(attachment2);
//			p.getWorkflowProcessingContext().getVorgang().addAttachment(attachment3);
//		}
//		catch (final Exception e) {
//			final Attachment attachment = AttachmentHelper.createAttachmentBody(e.toString().getBytes(), p.getWorkflowProcessingContext().getVorgang().getBearbeiter().getName(), "formcycle.log", p.getWorkflowProcessingContext().getVorgang().getMandant(), p.getWorkflowProcessingContext().getVorgang(), EAttachmentSource.WORKFLOW);
//			p.getWorkflowProcessingContext().getVorgang().addAttachment(attachment);
//		}
//		return new ProcessingResultStringValue("test");
//	}
//
//	@Override
//	public void initPlugin() throws FCPluginException {
//	}
//}
