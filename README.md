![Adyen Test Cards Android header][header.image]

# Adyen Test Cards Android
An Android app allowing to easily autofill Adyen test card number and other payment methods.

## Requirements
To run this app the minimum required Android version is 8.0 (API 26).

## Installation
Download the [APK file from the latest release](https://github.com/Adyen/adyen-testcards-android/releases/latest/download/adyen-test-cards.apk) and install it on your device.

Alternatively, you could clone this repository and run the app directly on your device.

## Usage
Open the app for the first time and it will prompt you to configure the Adyen Test Cards autofill service. Once done, you can autofill payment method fields as in the video below:

| Android                     | Web                 |
|-----------------------------|---------------------|
| ![Android][mystore.android] | ![Web][mystore.web] |

> [!NOTE]  
> Autofilling on Chrome does not work by default. To enable autofilling on Chrome follow these steps:
> 1. Open the settings in Chrome and navigate to "Autofill Services"
> 2. Select "Autofill using another service"
> 3. Restart Chrome
>
> On Chrome versions before 131 a flag needs to be enabled before the steps above can be followed: 
> 1. Open Chrome and go to `chrome://flags#enable-autofill-virtual-view-structure`
> 2. Enable the flag and restart Chrome

## Support
If you have a feature request, or spotted a bug or a technical problem, create a GitHub issue.

## Contributing
We strongly encourage you to contribute to our repository. Find out more in our [contribution guidelines](https://github.com/Adyen/.github/blob/master/CONTRIBUTING.md)

## License
This repository is available under the [MIT license](LICENSE).

[header.image]: https://github.com/user-attachments/assets/f38dad92-4f42-4a70-9e81-3f614c78f9a1
[mystore.android]: https://github.com/user-attachments/assets/e32af5eb-6920-48d2-a045-e478bd679a56
[mystore.web]: https://github.com/user-attachments/assets/4d223691-7e10-4ffb-a2c9-22268a5c99f5
