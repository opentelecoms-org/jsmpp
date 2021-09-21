package org.jsmpp.util;

import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterfaceVersionUtil {

  private static final Logger log = LoggerFactory.getLogger(InterfaceVersionUtil.class);

  public static InterfaceVersion getInterfaceVersion(OptionalParameter[] optionalParameters) {
    OptionalParameter.Sc_interface_version scVersion = OptionalParameters.get(OptionalParameter.Sc_interface_version.class, optionalParameters);
    if (scVersion != null) {
      log.debug("Other side reports SMPP interface version {}", scVersion);
      return InterfaceVersion.IF_50.min(InterfaceVersion.valueOf(scVersion.getValue()));
    }
    return InterfaceVersion.IF_34;
  }

  public static InterfaceVersion getNegotiatedInterfaceVersion(InterfaceVersion requested, OptionalParameter[] optionalParameters) {
    OptionalParameter.Sc_interface_version scVersion = OptionalParameters.get(OptionalParameter.Sc_interface_version.class, optionalParameters);
    if (scVersion != null) {
      log.debug("Other side reports SMPP interface version {}", scVersion);
      return requested.min(InterfaceVersion.valueOf(scVersion.getValue()));
    }
    log.debug("Use request SMPP interface version {}", requested);
    return requested;
  }
}
