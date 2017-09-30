/**
 * JWave is distributed under the MIT License (MIT); this file is part of.
 * <p>
 * Copyright (c) 2008-2017 Christian Scheiblich (cscheiblich@gmail.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.jiefly.multiparametermonitor.util.jwave.transforms.wavelets.symlets;

import com.example.jiefly.multiparametermonitor.util.jwave.transforms.wavelets.Wavelet;

/**
 * Symlet8 filter: near symmetric, orthogonal (orthonormal), biorthogonal.
 *
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 17.08.2014 14:28:42
 */
public class Symlet8 extends Wavelet {

    /**
     * Already orthonormal coefficients taken from Filip Wasilewski's webpage
     * http://wavelets.pybytes.com/wavelet/sym8/ Thanks!
     *
     * @author Christian Scheiblich (cscheiblich@gmail.com)
     * @date 17.08.2014 14:28:42
     */
    public Symlet8() {

        _name = "Symlet 8"; // name of the wavelet

        _transformWavelength = 2; // minimal wavelength of input signal

        _motherWavelength = 16; // wavelength of mother wavelet

        _scalingDeCom = new double[_motherWavelength];
        _scalingDeCom[0] = -0.0033824159510061256;
        _scalingDeCom[1] = -0.0005421323317911481;
        _scalingDeCom[2] = 0.03169508781149298;
        _scalingDeCom[3] = 0.007607487324917605;
        _scalingDeCom[4] = -0.1432942383508097;
        _scalingDeCom[5] = -0.061273359067658524;
        _scalingDeCom[6] = 0.4813596512583722;
        _scalingDeCom[7] = 0.7771857517005235;
        _scalingDeCom[8] = 0.3644418948353314;
        _scalingDeCom[9] = -0.05194583810770904;
        _scalingDeCom[10] = -0.027219029917056003;
        _scalingDeCom[11] = 0.049137179673607506;
        _scalingDeCom[12] = 0.003808752013890615;
        _scalingDeCom[13] = -0.01495225833704823;
        _scalingDeCom[14] = -0.0003029205147213668;
        _scalingDeCom[15] = 0.0018899503327594609;

        _waveletDeCom = new double[_motherWavelength];
        _waveletDeCom[0] = -0.0018899503327594609;
        _waveletDeCom[1] = -0.0003029205147213668;
        _waveletDeCom[2] = 0.01495225833704823;
        _waveletDeCom[3] = 0.003808752013890615;
        _waveletDeCom[4] = -0.049137179673607506;
        _waveletDeCom[5] = -0.027219029917056003;
        _waveletDeCom[6] = 0.05194583810770904;
        _waveletDeCom[7] = 0.3644418948353314;
        _waveletDeCom[8] = -0.7771857517005235;
        _waveletDeCom[9] = 0.4813596512583722;
        _waveletDeCom[10] = 0.061273359067658524;
        _waveletDeCom[11] = -0.1432942383508097;
        _waveletDeCom[12] = -0.007607487324917605;
        _waveletDeCom[13] = 0.03169508781149298;
        _waveletDeCom[14] = 0.0005421323317911481;
        _waveletDeCom[15] = -0.0033824159510061256;

        _scalingReCon = new double[_motherWavelength];
        _scalingReCon[0] = 0.0018899503327594609;
        _scalingReCon[1] = -0.0003029205147213668;
        _scalingReCon[2] = -0.01495225833704823;
        _scalingReCon[3] = 0.003808752013890615;
        _scalingReCon[4] = 0.049137179673607506;
        _scalingReCon[5] = -0.027219029917056003;
        _scalingReCon[6] = -0.05194583810770904;
        _scalingReCon[7] = 0.3644418948353314;
        _scalingReCon[8] = 0.7771857517005235;
        _scalingReCon[9] = 0.4813596512583722;
        _scalingReCon[10] = -0.061273359067658524;
        _scalingReCon[11] = -0.1432942383508097;
        _scalingReCon[12] = 0.007607487324917605;
        _scalingReCon[13] = 0.03169508781149298;
        _scalingReCon[14] = -0.0005421323317911481;
        _scalingReCon[15] = -0.0033824159510061256;

        _waveletReCon = new double[_motherWavelength];
        _waveletReCon[0] = -0.0033824159510061256;
        _waveletReCon[1] = 0.0005421323317911481;
        _waveletReCon[2] = 0.03169508781149298;
        _waveletReCon[3] = -0.007607487324917605;
        _waveletReCon[4] = -0.1432942383508097;
        _waveletReCon[5] = 0.061273359067658524;
        _waveletReCon[6] = 0.4813596512583722;
        _waveletReCon[7] = -0.7771857517005235;
        _waveletReCon[8] = 0.3644418948353314;
        _waveletReCon[9] = 0.05194583810770904;
        _waveletReCon[10] = -0.027219029917056003;
        _waveletReCon[11] = -0.049137179673607506;
        _waveletReCon[12] = 0.003808752013890615;
        _waveletReCon[13] = 0.01495225833704823;
        _waveletReCon[14] = -0.0003029205147213668;
        _waveletReCon[15] = -0.0018899503327594609;

    } // Symlet8

} // Symlet8
