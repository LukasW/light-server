package ch.smaug.light.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

@Path("info")
public class InfoRestService {

	@GET
	@Produces("text/plain")
	public String getInfoText() {
		try {
			final StringWriter sw = new StringWriter();
			final PrintWriter infoWriter = new PrintWriter(sw);
			// display a few of the available system information properties
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("HARDWARE INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("Serial Number     :  " + SystemInfo.getSerial());
			infoWriter.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
			infoWriter.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
			infoWriter.println("CPU Part          :  " + SystemInfo.getCpuPart());
			infoWriter.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
			infoWriter.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
			infoWriter.println("CPU Model Name    :  " + SystemInfo.getModelName());
			infoWriter.println("Processor         :  " + SystemInfo.getProcessor());
			infoWriter.println("Hardware Revision :  " + SystemInfo.getRevision());
			infoWriter.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
			infoWriter.println("Board Type        :  " + SystemInfo.getBoardType().name());

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("MEMORY INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
			infoWriter.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
			infoWriter.println("Free Memory       :  " + SystemInfo.getMemoryFree());
			infoWriter.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
			infoWriter.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
			infoWriter.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
			infoWriter.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
			infoWriter.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
			infoWriter.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("OPERATING SYSTEM INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("OS Name           :  " + SystemInfo.getOsName());
			infoWriter.println("OS Version        :  " + SystemInfo.getOsVersion());
			infoWriter.println("OS Architecture   :  " + SystemInfo.getOsArch());
			infoWriter.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
			infoWriter.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("JAVA ENVIRONMENT INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
			infoWriter.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
			infoWriter.println("Java Version      :  " + SystemInfo.getJavaVersion());
			infoWriter.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
			infoWriter.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("NETWORK INFO");
			infoWriter.println("----------------------------------------------------");

			// display some of the network information
			infoWriter.println("Hostname          :  " + NetworkInfo.getHostname());
			for (final String ipAddress : NetworkInfo.getIPAddresses()) {
				infoWriter.println("IP Addresses      :  " + ipAddress);
			}
			for (final String fqdn : NetworkInfo.getFQDNs()) {
				infoWriter.println("FQDN              :  " + fqdn);
			}
			for (final String nameserver : NetworkInfo.getNameservers()) {
				infoWriter.println("Nameserver        :  " + nameserver);
			}

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("CODEC INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
			infoWriter.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
			infoWriter.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());

			infoWriter.println("----------------------------------------------------");
			infoWriter.println("CLOCK INFO");
			infoWriter.println("----------------------------------------------------");
			infoWriter.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
			infoWriter.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
			infoWriter.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
			infoWriter.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
			infoWriter.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
			infoWriter.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
			infoWriter.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
			infoWriter.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
			infoWriter.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
			infoWriter.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
			infoWriter.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
			infoWriter.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());

			infoWriter.println();
			infoWriter.println();

			return sw.toString();
		} catch (NumberFormatException | IOException | InterruptedException | ParseException e) {
			return "Error: " + e.getMessage();
		}

	}
}
