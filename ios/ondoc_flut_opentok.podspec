#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
require 'yaml'
pubspec = YAML.load_file(File.join('..', 'pubspec.yaml'))
libraryVersion = pubspec['version'].gsub('+', '-')
openTokLibraryVersion = '2.21.3'

Pod::Spec.new do |s|
  s.name             = 'ondoc_flut_opentok'
  s.version          = '0.0.18'
  s.summary          = 'Ondoc Opentok Flutter library'
  s.description      = 'Ondoc Opentok Flutter library'
  s.homepage         = 'https://github.com/ninjasolutions/flutter_opentok'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Ondoc' => 'support@ondoc.me' }
  s.source           = { :path => '.' }
  s.swift_version = '5.0'
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'OpenTok', openTokLibraryVersion
  s.dependency 'SnapKit', '~> 5.0.1'
  s.static_framework = true

  s.ios.deployment_target = '10.0'

  s.prepare_command = <<-CMD
      echo // Generated file, do not edit > Classes/UserAgent.h
      echo "#define LIBRARY_VERSION @\\"#{libraryVersion}\\"" >> Classes/UserAgent.h
      echo "#define LIBRARY_NAME @\\"ondoc_flut_opentok\\"" >> Classes/UserAgent.h
      echo "#define OPENTOK_LIBRARY_VERSION @\\"#{openTokLibraryVersion}\\"" >> Classes/UserAgent.h
    CMD
end

