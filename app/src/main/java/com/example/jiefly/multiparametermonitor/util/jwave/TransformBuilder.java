/**
 * Create Transform objects ...
 *
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 14.03.2015 14:25:21
 * <p>
 * TransformBuilder.java
 */
package com.example.jiefly.multiparametermonitor.util.jwave;

import com.example.jiefly.multiparametermonitor.util.jwave.exceptions.JWaveException;
import com.example.jiefly.multiparametermonitor.util.jwave.exceptions.JWaveFailure;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.BasicTransform;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.DiscreteFourierTransform;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.FastWaveletTransform;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.ShiftingWaveletTransform;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.WaveletPacketTransform;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.wavelets.Wavelet;
import com.example.jiefly.multiparametermonitor.util.jwave.transforms.wavelets.WaveletBuilder;

/**
 * Class for creating and identifying Transform object.
 *
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 14.03.2015 14:25:21
 */
public class TransformBuilder {

    /**
     * Create a Transform object by a given string and a given Wavelet object.
     * Look into each Transform for matching string identifier. By the way the
     * method requires Java 7, due to the switch statement on a string. *rofl*
     *
     * @param transformName identifier as stored in Transform object
     * @param wavelet
     * @return a matching object of type Transform
     * @author Christian Scheiblich (cscheiblich@gmail.com)
     * @date 14.03.2015 14:35:12
     */
    static public Transform create(String transformName, Wavelet wavelet) {

        BasicTransform basicTransform = null;

        try {

            switch (transformName) {

                case "Discrete Fourier Transform":
                    basicTransform = new DiscreteFourierTransform();
                    break;

                case "Fast Wavelet Transform":
                    basicTransform = new FastWaveletTransform(wavelet);
                    break;

                case "Wavelet Packet Transform":
                    basicTransform = new WaveletPacketTransform(wavelet);
                    break;
                case "Shifting Wavelet Transform":
                    basicTransform = new ShiftingWaveletTransform(wavelet);
                    break;

                default:

                    throw new JWaveFailure(
                            "TransformBuilder::create - unknown type of transform for given string!");

            } // switch

        } catch (JWaveException e) {

            e.showMessage();
            e.printStackTrace();

        } // try

        return new Transform(basicTransform);

    } // create

    /**
     * Create a Transform object by a given string and a given string for a
     * Wavelet object. Look into each Transform and Wavelet for matching string
     * identifier.
     *
     * @param transformName identifier as stored in Transform object
     * @param waveletName
     * @return identifier as stored in Wavelet object
     * @author Christian Scheiblich (cscheiblich@gmail.com)
     * @date 14.03.2015 14:37:30
     */
    static public Transform create(String transformName, String waveletName) {

        return create(transformName, WaveletBuilder.create(waveletName));

    } // create

    /**
     * Returns the identifier string of a given Transform object.
     *
     * @param transform string identifier of the given Transform object
     * @return a string as the identifier of the given Transform object
     * @author Christian Scheiblich (cscheiblich@gmail.com)
     * @date 14.03.2015 14:34:20
     */
    static public String identify(Transform transform) {

        BasicTransform basicTransform = transform.getBasicTransform();
        return basicTransform.getName();

    } // identify

} // class
